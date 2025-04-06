package com.tavolo.platform.iam.interfaces.rest.transform;

import com.tavolo.platform.iam.domain.model.commands.SignInCommand;
import com.tavolo.platform.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(resource.username(), resource.password());
    }
}
