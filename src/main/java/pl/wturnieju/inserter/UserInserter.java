package pl.wturnieju.inserter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;

import lombok.RequiredArgsConstructor;
import pl.wturnieju.repository.UserRepository;
import pl.wturnieju.service.IUserService;

@RequiredArgsConstructor
public class UserInserter {

    private final IUserService userService;

    private final UserRepository userRepository;

    private Map<String, String> createEmailsAndPasswords() {
        Map<String, String> emailsAndPasswords = new HashMap<>();

        emailsAndPasswords.put("aukjan@yahoo.com", "EFcYcdFXT7GCAa1,");
        emailsAndPasswords.put("liedra@yahoo.com", "ZF6aeCnTyW7RAa1,");
        emailsAndPasswords.put("kmiller@msn.com", "aeczA2wav9bAAa1,");
        emailsAndPasswords.put("frosal@att.net", "7tGH76nD9LabAa1,");
        emailsAndPasswords.put("guialbu@comcast.net", "GNyS4WDK28a4Aa1,");
        emailsAndPasswords.put("uncled@verizon.net", "jP7VyubVkhP8Aa1,");
        emailsAndPasswords.put("kempsonc@hotmail.com", "JJWvKeAYKU7XAa1,");
        emailsAndPasswords.put("mfleming@msn.com", "mnKBKbkwWjJLAa1,");
        emailsAndPasswords.put("qmacro@mac.com", "kSRKcf3fwj63Aa1,");
        emailsAndPasswords.put("british@optonline.net", "6QKAGNd7F4NnAa1,");
        emailsAndPasswords.put("danny@live.com", "QKYbC6NAeNAuAa1,");
        emailsAndPasswords.put("carcus@me.com", "LXbwHc3yEakCAa1,");

        return emailsAndPasswords;
    }

    public void insertUsersToDatabase() {
        createEmailsAndPasswords().forEach(userService::create);
        setUserDetails();
    }

    private void setUserDetails() {
        var iterator = createNamesAndSurnames().iterator();

        userRepository.findAll().forEach(user -> {
            var nameAndSurname = iterator.next();
            user.setName(nameAndSurname.getLeft());
            user.setSurname(nameAndSurname.getRight());
            userRepository.save(user);
        });
    }

    private List<ImmutablePair<String, String>> createNamesAndSurnames() {
        List<ImmutablePair<String, String>> nameAndSurname = new ArrayList<>();

        nameAndSurname.add(ImmutablePair.of("Jaden", "Brock"));
        nameAndSurname.add(ImmutablePair.of("Gina", "Graham"));
        nameAndSurname.add(ImmutablePair.of("Alfredo", "Velasquez"));
        nameAndSurname.add(ImmutablePair.of("Miriam", "Short"));
        nameAndSurname.add(ImmutablePair.of("Sherlyn", "Valencia"));
        nameAndSurname.add(ImmutablePair.of("Joseph", "Stein"));
        nameAndSurname.add(ImmutablePair.of("Mackenzie", "Hardin"));
        nameAndSurname.add(ImmutablePair.of("Kira", "Daniel"));
        nameAndSurname.add(ImmutablePair.of("Simone", "Maddox"));
        nameAndSurname.add(ImmutablePair.of("Rudy", "Diaz"));
        nameAndSurname.add(ImmutablePair.of("Kaila", "Herring"));
        nameAndSurname.add(ImmutablePair.of("Elise", "Rose"));

        return nameAndSurname;
    }


}
