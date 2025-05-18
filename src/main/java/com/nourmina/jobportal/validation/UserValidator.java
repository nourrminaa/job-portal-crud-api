// UserValidator.java
package com.nourmina.jobportal.validation;

import com.nourmina.jobportal.model.User;
import com.nourmina.jobportal.exception.BadRequestException;

public class UserValidator {
    public static void validate(User user) {
        if (user == null) throw new BadRequestException("User cannot be null");
        if (user.getEmail() == null || user.getEmail().isBlank())
            throw new BadRequestException("User email is required");
        if (!user.getEmail().contains("@"))
            throw new BadRequestException("User email is invalid");
        if (user.getFirstName() == null || user.getFirstName().isBlank())
            throw new BadRequestException("User first name is required");
    }
}
