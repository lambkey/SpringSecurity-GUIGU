package lamb.key.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JoinYang
 * @date 2022/5/6 19:17
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Admin {
    private Integer id;
    private String login_acct;
    private String user_pswd;
    private String user_name;
    private String email;

}
