package pl.wturnieju.dto.mapping;

import pl.wturnieju.dto.UserDTO;
import pl.wturnieju.model.User;

public class UserMapping {

    public static UserDTO map(User user) {
        if (user == null) {
            return null;
        }

        var dto = new UserDTO();

        dto.setEmail(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());

        return dto;
    }

}
