package cn.xianbin.commonapi.vo;

import lombok.Getter;

@Getter
public class JdbcSourceInfo {

    private String jdbcUrl;

    private String username;

    private String password;

    private String type;

    private JdbcSourceInfo(String jdbcUrl, String username, String password,String type) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.type = type;
    }


    public static final class JdbcSourceInfoBuilder {
        private String jdbcUrl;
        private String username;
        private String password;
        private String type;

        private JdbcSourceInfoBuilder() {
        }

        public static JdbcSourceInfoBuilder builder() {
            return new JdbcSourceInfoBuilder();
        }

        public JdbcSourceInfoBuilder withJdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
            return this;
        }

        public JdbcSourceInfoBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public JdbcSourceInfoBuilder withPassword(String password) {
            this.password = password;
            return this;
        }
        public JdbcSourceInfoBuilder withType(String type) {
            this.type = type;
            return this;
        }
        public JdbcSourceInfo build() {
            return new JdbcSourceInfo(jdbcUrl, username, password,type);
        }
    }
}
