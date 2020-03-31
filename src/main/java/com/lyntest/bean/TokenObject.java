package com.lyntest.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenObject {


    /**员工code*/
    private String empCode;
    /**姓名**/
    private String name;
    /**到期时间*/
    private Long expiresSecond;
    /**加密后的token*/
    private String accessToken;
}
