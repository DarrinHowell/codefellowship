package com.darrinhowell.codefellowship;

public class ApplicationUser {
    String username;
    String password;
    String firstName;
    String lastName;
    String dateOfBirth;
    String bio;

    public ApplicationUser (String username, String password, String firstName, String lastName,
                           String dateOfBirth, String bio) {

        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;

    }

    public ApplicationUser () {}
}
