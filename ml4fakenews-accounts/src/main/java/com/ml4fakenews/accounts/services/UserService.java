package com.ml4fakenews.accounts.services;

import com.ml4fakenews.accounts.dtos.RegistrationData;
import com.ml4fakenews.accounts.entities.User;
import com.ml4fakenews.accounts.repositories.UserRepository;
import com.ml4fakenews.accounts.security.JwtResponse;
import com.ml4fakenews.accounts.security.JwtTokenUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RabbitTemplate queueClient;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;


    @Autowired
    public UserService(UserRepository userRepository, RabbitTemplate queueClient, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.queueClient = queueClient;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public List<User> getAllAccounts() {
        List<User> allUsers = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return allUsers;
    }

    public User getAccountById(int id) {
        return userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }



    @Override
    public User loadUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        System.out.println(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika o podanym loginie!")));

        return userRepository.findByUsername(username).get();
    }

    public User registerAccount(RegistrationData registrationData) {
        String encodedPassword = bCryptPasswordEncoder.encode(registrationData.getPassword());
        User newUser = new User(registrationData.getUsername(), encodedPassword, registrationData.getEmail());
        userRepository.save(newUser);
        queueClient.convertAndSend("account_created", newUser.getEmail());
        return newUser;

    }

    public boolean isAccountAlreadyCreated(String email, String name) {
        Optional<User> account = userRepository.findUserByEmailOrUsername(email, name);
        return account.isPresent();
    }

    public boolean isPasswordCorrect(RegistrationData data) {
        String password = data.getPassword();
        Pattern regex = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[#?!@$%^&*-]).{8,20}$");
        return regex.matcher(password).matches();
    }


    public ResponseEntity<?> authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateJwtToken(authentication);
        LocalDateTime expirationDate = jwtTokenUtil.getExpirationDate(jwt);
        User user = (User) authentication.getPrincipal();
        List list = user.getAuthorities().stream().map(auth -> auth.getAuthority()
        ).collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(user.getId(), user.getUsername(), list, jwt, expirationDate));

    }

}
