package com.changgou.token;

import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

/*****
 * @Author: www.itheima
 * @Date: 2019/7/7 13:48
 * @Description: com.changgou.token
 *  使用公钥解密令牌数据
 ****/
public class ParseJwtTest {

    /***
     * 校验令牌
     */
    @Test
    public void testParseToken(){
        //令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJhcHAiXSwibmFtZSI6bnVsbCwiaWQiOm51bGwsImV4cCI6MjAxMTQ4OTk1OSwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iLCJ1c2VyIl0sImp0aSI6ImZmYjIyMTQxLTFiMDYtNGIwYi1hY2U2LTU4ZTcyZDBiNmQ2MSIsImNsaWVudF9pZCI6InN6aXRoZWltYSIsInVzZXJuYW1lIjoieWV6dXNoZSJ9.VSwb-xkCtQqSLwvWKymmsuK1Dz6UPWEOMCZxtWWp8XyHuAYhV1kFB_OEAHFRMOZuAxuMgckeTx1hr7NickXq-NaHPqnR0ziE7RPERFv1iBklAzma1O9c5ryPMzHJx6CZuUWtLhzPeDIRhpc3VtByWXEXzzi28uyJnPCTXu4coMd-nl7r8_b60hgtzxizigVfM4LKu9YJ3qpDvU1npVLa7NDsVAZXWe8FoM9j9o6lBMUyAsJsr0bZxuRxhtf3krkdDxsBaAp9_WaRXGbMHNNBia0reRaMED8R2TP12zQ3d9q-QTs6wcpYSuy30lrg5ERuMNwoROAOD9TrpvkVPzxZ_g";

        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjuFTbLUmbzFm5dxA0RKmh8WI1j2zHpvup40RTjjep8HHR85jrCOu+7vhXl6NdFajh2vPeqcsK8FNrNM9P0uTE0VAHoShtnifafBl8Q5mKFn20CFeQ9rJBmRFoBNXPsS3tIPbMVx8DPfWGAmZDzp2SSaSdyrf9ukCd071LTM77jYsjh9r64v8h3evjieNCBJSrgtFWuMOL2nXnhQBGu9rma9nzotLeKyMszCRkf2bd2n+91V7Ad8JPz9k+a5Jw18NkzSVZkQrV6iOjOw+zs7GbB5n7CFBLMmUXgQ5PdTWLQNf/OavcvEvxhhnzrj6PZ9ha4m2+ll7Urwu/+2w1+aSKwIDAQAB-----END PUBLIC KEY-----";

        //校验Jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publickey));

        //获取Jwt原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
