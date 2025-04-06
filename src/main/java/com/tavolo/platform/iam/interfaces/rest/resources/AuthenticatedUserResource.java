package com.tavolo.platform.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(Long id, String username, String token) {
}
