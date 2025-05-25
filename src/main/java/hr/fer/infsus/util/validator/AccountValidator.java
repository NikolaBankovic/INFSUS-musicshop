package hr.fer.infsus.util.validator;

import hr.fer.infsus.exception.PasswordComplexityException;
import hr.fer.infsus.exception.SamePasswordException;
import hr.fer.infsus.model.User;
import hr.fer.infsus.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountValidator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void usernameTaken(final String username) {
        if(userRepository.existsByUsername(username)) {
            throw new EntityExistsException("This username is already taken!");
        }
    }

    public void usernameTaken(final User user, final String username) {
        if(!user.getUsername().equals(username) && userRepository.existsByUsername(username)) {
            throw new EntityExistsException("This username is already taken!");
        }
    }

    public void usernameChangeValid(final User user, final String username) {
        if(userRepository.existsByUsername(username)) {
            throw new EntityExistsException("This username is already taken!");
        }
        else {
            if(user.getUsername().equals(username)) {
                throw new EntityExistsException("New username cannot be the same as old username!");
            }
        }

    }

    public void emailExists(final String email) {
        if(userRepository.existsByEmail(email)) {
            throw new EntityExistsException("This email address is already in use!");
        }
    }

    public void emailExists(final User user, final String email) {
        if(!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new EntityExistsException("This email address is already in use!");
        }
    }

    public void passwordChangeValid(final Long id, final String newPassword) throws SamePasswordException, PasswordComplexityException {
        final User user = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("User with ID(%d) doesn't exist!", id)));
        if(newPassword.length() < 8) {
            throw new PasswordComplexityException("Password must be at least 8 characters");
        }
        if(passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new SamePasswordException("New password cannot be the same as old password!");
        }

    }

    public void passwordChangeValid(final String username, final String newPassword) throws SamePasswordException {
        final User user = userRepository.findByUsername(username);
        if(user != null && passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new SamePasswordException("New password cannot be the same as old password!");
        }

    }

    public void passwordChangeValid(final User user, final String newPassword) throws SamePasswordException {
        if(passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new SamePasswordException("New password cannot be the same as old password!");
        }
    }


}
