package com.bettercloud.vault.api;

import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import com.bettercloud.vault.response.VaultResponse;
import com.bettercloud.vault.rest.Rest;
import com.bettercloud.vault.rest.RestResponse;

/**
 * <p>The implementing class for operations on REST endpoints, under the "Leases" section of the Vault HTTP API
 * docs (https://www.vaultproject.io/docs/http/index.html).</p>
 *
 * <p>This class is not intended to be constructed directly.  Rather, it is meant to used by way of
 * <code>Vault</code> in a DSL-style builder pattern.  See the Javadoc comments of each <code>public</code>
 * method for usage examples.</p>
 */
public class Leases {

    private final VaultConfig config;

    public Leases(final VaultConfig config) {
        this.config = config;
    }

    /**
     * <p>Immediately revokes a secret associated with a given lease.  E.g.:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final VaultResponse response = vault.leases().revoke("7c63da27-a56b-3e3b-377d-ef74630a6d0b");
     * assertEquals(204, response.getRestResponse().getStatus());
     * }</pre>
     * </blockquote>
     *
     * @param leaseId A lease ID associated with the secret to be revoked
     * @return The response information returned from Vault
     * @throws VaultException If an error occurs, or unexpected reponse received from Vault
     */
    public VaultResponse revoke(final String leaseId) throws VaultException {
        int retryCount = 0;
        while (true) {
            try {
                final RestResponse restResponse = new Rest()//NOPMD
                        .url(config.getAddress() + "/v1/sys/revoke/" + leaseId)
                        .header("X-Vault-Token", config.getToken())
                        .connectTimeoutSeconds(config.getOpenTimeout())
                        .readTimeoutSeconds(config.getReadTimeout())
                        .sslPemUTF8Set(config.getSslPemUTF8Set())
                        .sslVerification(config.isSslVerify() != null ? config.isSslVerify() : null)
                        .put();

                // Validate response
                if (restResponse.getStatus() != 204) {
                    throw new VaultException("Expecting HTTP status 204, but instead receiving " + restResponse.getStatus(), restResponse.getStatus());
                }
                return new VaultResponse(restResponse, retryCount);
            } catch (Exception e) {
                // If there are retries to perform, then pause for the configured interval and then execute the loop again...
                if (retryCount < config.getMaxRetries()) {
                    retryCount++;
                    try {
                        final int retryIntervalMilliseconds = config.getRetryIntervalMilliseconds();
                        Thread.sleep(retryIntervalMilliseconds);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } else if (e instanceof VaultException) {
                    // ... otherwise, give up.
                    throw (VaultException) e;
                } else {
                    throw new VaultException(e);
                }
            }
        }
    }

    /**
     * <p>Revokes all secrets (via a lease ID prefix) or tokens (via the tokens' path property) generated under a
     * given prefix immediately.  This requires sudo capability and access to it should be tightly controlled as it
     * can be used to revoke very large numbers of secrets/tokens at once.  E.g.:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final VaultResponse response = vault.leases().revokePrefix("aws");
     * assertEquals(204, response.getRestResponse().getStatus());
     * }</pre>
     * </blockquote>
     *
     * @param prefix A Vault path prefix, for which all secrets beneath it should be revoked
     * @return The response information returned from Vault
     * @throws VaultException If an error occurs, or unexpected reponse received from Vault
     */
    public VaultResponse revokePrefix(final String prefix) throws VaultException {
        int retryCount = 0;
        while (true) {
            try {
                final RestResponse restResponse = new Rest()//NOPMD
                        .url(config.getAddress() + "/v1/sys/revoke-prefix/" + prefix)
                        .header("X-Vault-Token", config.getToken())
                        .connectTimeoutSeconds(config.getOpenTimeout())
                        .readTimeoutSeconds(config.getReadTimeout())
                        .sslPemUTF8Set(config.getSslPemUTF8Set())
                        .sslVerification(config.isSslVerify() != null ? config.isSslVerify() : null)
                        .put();

                // Validate response
                if (restResponse.getStatus() != 204) {
                    throw new VaultException("Expecting HTTP status 204, but instead receiving " + restResponse.getStatus(), restResponse.getStatus());
                }
                return new VaultResponse(restResponse, retryCount);
            } catch (Exception e) {
                // If there are retries to perform, then pause for the configured interval and then execute the loop again...
                if (retryCount < config.getMaxRetries()) {
                    retryCount++;
                    try {
                        final int retryIntervalMilliseconds = config.getRetryIntervalMilliseconds();
                        Thread.sleep(retryIntervalMilliseconds);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } else if (e instanceof VaultException) {
                    // ... otherwise, give up.
                    throw (VaultException) e;
                } else {
                    throw new VaultException(e);
                }
            }
        }
    }

    /**
     * <p>Revokes all secrets or tokens generated under a given prefix immediately. Unlike revokePrefix(String),
     * this method ignores backend errors encountered during revocation. This is potentially very dangerous and should
     * only be used in specific emergency situations where errors in the backend or the connected backend service
     * prevent normal revocation.  By ignoring these errors, Vault abdicates responsibility for ensuring that the
     * issued credentials or secrets are properly revoked and/or cleaned up. Access to this endpoint should be tightly
     * controlled.  E.g.:</p>
     *
     * <blockquote>
     * <pre>{@code
     * final VaultResponse response = vault.leases().revokePrefix("aws");
     * assertEquals(204, response.getRestResponse().getStatus());
     * }</pre>
     * </blockquote>
     *
     * @param prefix A Vault path prefix, for which all secrets beneath it should be revoked
     * @return The response information returned from Vault
     * @throws VaultException If an error occurs, or unexpected reponse received from Vault
     */
    public VaultResponse revokeForce(final String prefix) throws VaultException {
        int retryCount = 0;
        while (true) {
            try {
                final RestResponse restResponse = new Rest()//NOPMD
                        .url(config.getAddress() + "/v1/sys/revoke-force/" + prefix)
                        .header("X-Vault-Token", config.getToken())
                        .connectTimeoutSeconds(config.getOpenTimeout())
                        .readTimeoutSeconds(config.getReadTimeout())
                        .sslPemUTF8Set(config.getSslPemUTF8Set())
                        .sslVerification(config.isSslVerify() != null ? config.isSslVerify() : null)
                        .put();

                // Validate response
                if (restResponse.getStatus() != 204) {
                    throw new VaultException("Expecting HTTP status 204, but instead receiving " + restResponse.getStatus(), restResponse.getStatus());
                }
                return new VaultResponse(restResponse, retryCount);
            } catch (Exception e) {
                // If there are retries to perform, then pause for the configured interval and then execute the loop again...
                if (retryCount < config.getMaxRetries()) {
                    retryCount++;
                    try {
                        final int retryIntervalMilliseconds = config.getRetryIntervalMilliseconds();
                        Thread.sleep(retryIntervalMilliseconds);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } else if (e instanceof VaultException) {
                    // ... otherwise, give up.
                    throw (VaultException) e;
                } else {
                    throw new VaultException(e);
                }
            }
        }
    }

}
