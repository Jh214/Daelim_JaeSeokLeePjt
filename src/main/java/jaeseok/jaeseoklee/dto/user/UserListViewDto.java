package jaeseok.jaeseoklee.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserListViewDto {
    private String userId;
    private String userRealName;

    public UserListViewDto(String userId, String userRealName) {
        this.userId = userId;
        this.userRealName = userRealName;
    }
}
