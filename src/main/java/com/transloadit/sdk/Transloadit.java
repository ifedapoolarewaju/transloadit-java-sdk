package com.transloadit.sdk;

import com.transloadit.sdk.async.AssemblyProgressListener;
import com.transloadit.sdk.async.UploadProgressListener;
import com.transloadit.sdk.async.AsyncAssembly;
import com.transloadit.sdk.exceptions.RequestException;
import com.transloadit.sdk.exceptions.LocalOperationException;
import com.transloadit.sdk.response.AssemblyResponse;
import com.transloadit.sdk.response.ListResponse;
import com.transloadit.sdk.response.Response;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


/**
 * This class serves as a client interface to the Transloadit API
 */
public class Transloadit {
    public static final String DEFAULT_HOST_URL = "https://api2.transloadit.com";
    String key;
    String secret;
    long duration;
    private String hostUrl;
    boolean shouldSignRequest;
    protected int retryAttempts = 3;  // default value

    /**
     * A new instance to transloadit client
     *
     * @param key User's transloadit key
     * @param secret User's transloadit secret.
     * @param duration for how long (in seconds) the request should be valid.
     * @param hostUrl the host url to the transloadit service.
     */
    public Transloadit(String key, @Nullable String secret, long duration, String hostUrl) {
        this.key = key;
        this.secret = secret;
        this.duration = duration;
        this.hostUrl = hostUrl;
        shouldSignRequest = secret != null;
    }

    /**
     * A new instance to transloadit client
     *
     * @param key User's transloadit key
     * @param secret User's transloadit secret.
     * @param duration for how long (in seconds) the request should be valid.
     */
    public Transloadit(String key, String secret, long duration) {
        this(key, secret, duration, DEFAULT_HOST_URL);
    }

    /**
     * A new instance to transloadit client
     *
     * @param key User's transloadit key
     * @param secret User's transloadit secret.
     * @param hostUrl the host url to the transloadit service.
     */
    public Transloadit(String key, String secret, String hostUrl) {
        this(key, secret, 5 * 60, hostUrl);
    }

    /**
     * A new instance to transloadit client
     *
     * @param key User's transloadit key
     * @param secret User's transloadit secret.
     */
    public Transloadit(String key, String secret) {
        this(key, secret, 5 * 60, DEFAULT_HOST_URL);
    }

    /**
     * Enable/Disable request signing.
     * @param flag the boolean value to set it to.
     * @throws LocalOperationException if something goes wrong while running non-http operations.
     */
    public void setRequestSigning(boolean flag) throws LocalOperationException {
        if (flag && secret == null) {
            throw new LocalOperationException("Cannot enable request signing with null secret.");
        } else {
            shouldSignRequest = flag;
        }
    }

    /**
     * Adjusts number of retry attempts that should be taken if a "RATE_LIMIT_REACHED" error appears
     * during assembly processing.
     * Default value for every Transloadit instance is 3 retries.
     * @param retryAttempts number of retry attempts
     * @throws LocalOperationException if provided number is negative
     */
    public void setRetryAttempts(int retryAttempts) throws LocalOperationException{
        if (retryAttempts < 0) {
            throw new LocalOperationException("No negative number of retry Attempts possible.");
        } else {
            this.retryAttempts = retryAttempts;
        }
    }

    /**
     * Returns number of retry attempts that should be taken in case of a "RATE_LIMIT_REACHED" error appears
     * during assembly processing.
     * @return number of retry attempts
     */
    public int getRetryAttempts() {
        return retryAttempts;
    }

    /**
     *
     * @return the host url of the Transloadit server.
     */
    public String getHostUrl() {
        return hostUrl;
    }

    /**
     * Returns an Assembly instance that can be used to create a new assembly.
     *
     * @return {@link Assembly}
     */
    public Assembly newAssembly() {
        return new Assembly(this);
    }

    /**
     * Returns an AsyncAssembly instance that can be used to create a new assembly asynchronously.
     * This method is good for running assemblies in the background
     *
     * @param listener an implementation of {@link UploadProgressListener} to serve as a callback
     *                 for the asynchronous assembly.
     * @return {@link AsyncAssembly}
     */
    public AsyncAssembly newAssembly(UploadProgressListener listener) {
        return new AsyncAssembly(this, listener);
    }

    /**
     * Returns an AsyncAssembly instance that can be used to create a new assembly asynchronously.
     * This method is good for running assemblies in the background
     *
     * @param listener an implementation of {@link AssemblyProgressListener} to serve as a callback
     *                 for the asynchronous assembly.
     * @deprecated use {@link #newAssembly(UploadProgressListener)} instead
     * @return {@link AsyncAssembly}
     */
    public AsyncAssembly newAssembly(AssemblyProgressListener listener) {
        return new AsyncAssembly(this, listener);
    }

    /**
     * Returns a single assembly.
     *
     * @param id id of the Assembly to retrieve.
     * @return {@link AssemblyResponse}
     * @throws RequestException if request to transloadit server fails.
     * @throws LocalOperationException if something goes wrong while running non-http operations.
     */
    public AssemblyResponse getAssembly(String id) throws RequestException, LocalOperationException {
        Request request = new Request(this);
        return new AssemblyResponse(request.get("/assemblies/" + id));
    }

    /**
     * Returns a single assembly.
     *
     * @param url full url of the Assembly.
     * @return {@link AssemblyResponse}
     * @throws RequestException if request to transloadit server fails.
     * @throws LocalOperationException if something goes wrong while running non-http operations.
     */
    public AssemblyResponse getAssemblyByUrl(String url)
            throws RequestException, LocalOperationException {
        Request request = new Request(this);
        return new AssemblyResponse(request.get(url));
    }

    /**
     * cancels a running assembly.
     *
     * @param url full url of the Assembly.
     * @return {@link AssemblyResponse}
     * @throws RequestException if request to transloadit server fails.
     * @throws LocalOperationException if something goes wrong while running non-http operations.
     */
    public AssemblyResponse cancelAssembly(String url)
            throws RequestException, LocalOperationException {
        Request request = new Request(this);
        return new AssemblyResponse(request.delete(url, new HashMap<String, Object>()));
    }

    /**
     * Returns a list of all assemblies under the user account
     *
     * @param options {@link Map} extra options to send along with the request.
     * @return {@link ListResponse}
     * @throws RequestException if request to transloadit server fails.
     * @throws LocalOperationException if something goes wrong while running non-http operations.
     */
    public ListResponse listAssemblies(Map<String, Object> options)
            throws RequestException, LocalOperationException {
        Request request = new Request(this);
        return new ListResponse(request.get("/assemblies", options));
    }

    public ListResponse listAssemblies() throws RequestException, LocalOperationException {
        return listAssemblies(new HashMap<String, Object>());
    }

    /**
     * Returns a Template instance that can be used to create a new template.
     * @param name name of the template.
     *
     * @return {@link Template}
     */
    public Template newTemplate(String name) {
        return new Template(this, name);
    }

    /**
     * Returns a single template.
     *
     * @param id id of the template to retrieve.
     * @return {@link Response}
     *
     * @throws RequestException if request to transloadit server fails.
     * @throws LocalOperationException if something goes wrong while running non-http operations.
     */
    public Response getTemplate(String id) throws RequestException, LocalOperationException {
        Request request = new Request(this);
        return new Response(request.get("/templates/" + id));
    }

    /**
     * Updates the template with the specified id.
     *
     * @param id id of the template to update
     * @param options a Map of options to update/add.
     * @return {@link Response}
     *
     * @throws RequestException if request to transloadit server fails.
     * @throws LocalOperationException if something goes wrong while running non-http operations.
     */
    public Response updateTemplate(String id, Map<String, Object> options)
            throws RequestException, LocalOperationException {
        Request request = new Request(this);
        return new Response(request.put("/templates/" + id, options));
    }

    /**
     * Deletes a template.
     *
     * @param id id of the template to delete.
     * @return {@link Response}
     *
     * @throws RequestException if request to transloadit server fails.
     * @throws LocalOperationException if something goes wrong while running non-http operations.
     */
    public Response deleteTemplate(String id)
            throws RequestException, LocalOperationException {
        Request request = new Request(this);
        return new Response(request.delete("/templates/" + id, new HashMap<String, Object>()));
    }

    /**
     * Returns a list of all templates under the user account
     *
     * @param options {@link Map} extra options to send along with the request.
     * @return {@link ListResponse}
     *
     * @throws RequestException if request to transloadit server fails.
     * @throws LocalOperationException if something goes wrong while running non-http operations.
     */
    public ListResponse listTemplates(Map<String, Object> options)
            throws RequestException, LocalOperationException {
        Request request = new Request(this);
        return new ListResponse(request.get("/templates", options));
    }

    /**
     * Returns a list of all templates under the user account
     *
     * @return {@link ListResponse}
     *
     * @throws RequestException if request to transloadit server fails.
     * @throws LocalOperationException if something goes wrong while running non-http operations.
     */
    public ListResponse listTemplates()
            throws RequestException, LocalOperationException {
        return listTemplates(new HashMap<String, Object>());
    }

    /**
     * Returns the bill for the month specified.
     *
     * @param month for which bill to retrieve.
     * @param year for which bill to retrieve.
     * @return {@link Response}
     *
     * @throws RequestException if request to transloadit server fails.
     * @throws LocalOperationException if something goes wrong while running non-http operations.
     */
    public Response getBill(int month, int year)
            throws RequestException, LocalOperationException {
        Request request = new Request(this);
        return new Response(request.get("/bill/" + year + String.format("-%02d", month)));
    }
}
