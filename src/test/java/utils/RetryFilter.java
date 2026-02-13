package utils;

import exceptions.FrameworkException;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class RetryFilter implements Filter {
    private static final int MAX_RETRIES = 3;

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        Response response = ctx.next(requestSpec, responseSpec);
        int retryCount = 0;

        // Added a null check here to prevent the NPE you just saw
        while ((response == null || isIntermittentError(response)) && retryCount < MAX_RETRIES) {
            retryCount++;
            System.out.println("⚠️ Attempt " + retryCount + " failed. Retrying...");

            try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

            response = ctx.next(requestSpec, responseSpec);
        }

        if (response == null) {
            throw new FrameworkException("❌ API Response is null after " + MAX_RETRIES + " retries.");
        }

        return response;
    }

    private boolean isIntermittentError(Response response) {
        int code = response.getStatusCode();
        // ONLY retry for server errors (5xx) and rate limiting (429)
        return code == 502 || code == 503 || code == 504 || code == 429;
    }
}