package org.yuemi.mmomechanics.api;

import org.jetbrains.annotations.NotNull;

/**
 * Entry point for accessing the MmoMechanics API.
 *
 * Consumers should depend on this interface, not implementation details.
 */
public interface MmoMechanicsApiProvider {

    /**
     * Gets the active instance of the MmoMechanicsApi.
     *
     * @return active API instance
     */
    @NotNull
    MmoMechanicsApi getApi();
}
