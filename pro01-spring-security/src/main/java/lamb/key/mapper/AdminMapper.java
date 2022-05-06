package lamb.key.mapper;

import lamb.key.entity.Admin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author JoinYang
 * @date 2022/5/6 19:18
 */
public interface AdminMapper extends Mapper<Admin> {
    @Select("SELECT * FROM T_ADMIN WHERE LOGIN_ACCT =#{LOGIN_ACCT}")
    Admin getAdminByAcctName(@Param("LOGIN_ACCT") String name);
}
