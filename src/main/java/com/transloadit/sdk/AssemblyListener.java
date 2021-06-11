package com.transloadit.sdk;

import com.transloadit.sdk.response.AssemblyResponse;

/**
 * Interface for a Listener, which tracks the progress in Assembly execution.
 */
public interface AssemblyListener {
    /**
     * Callback to be executed when the Assembly execution is done executing.
     * This encompasses any kind of termination of the assembly.
     * Including when the assembly aborts due to failure.
     *
     * @param response {@link AssemblyResponse} response with the updated status of the assembly.
     */
    void onAssemblyFinished(AssemblyResponse response);

    /**
     * Callback to be executed if the socket connection throws an Error.
     * This encompasses any kind of termination of the assembly.
     *
     * @param error {@link Exception} the error thrown during the socket connection.
     */
    void onError(Exception error);

    /**
     * Callback to be exectued if the Assembly's files metadata has been extracted.
     */
    void onMetadataExtracted();

    /**
     * Callback to be executed if the Assembly's files have been uploaded.
     */
    void onAssemblyUploadFinished();

    /**
     * Callback to be executed if one of the Assembly's files has been uploaded.
     * @param fileName Name of the file, which has been successfully uploaded.
     */
    void onFileUploadFinished(String fileName);


}
