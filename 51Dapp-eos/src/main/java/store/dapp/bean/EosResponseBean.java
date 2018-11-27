package store.dapp.bean;

import java.util.List;

/**
 * @Auther: liulu3
 * @Date: 2018/11/1 21:02
 * @Description: 查询用户基本信息 response
 */
public class EosResponseBean {
    private String creator; //父账号
    private String create_timestamp; //创建时间
    private List<PermissionsBean> permissions; //权限组


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreate_timestamp() {
        return create_timestamp;
    }

    public void setCreate_timestamp(String create_timestamp) {
        this.create_timestamp = create_timestamp;
    }

    public List<PermissionsBean> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionsBean> permissions) {
        this.permissions = permissions;
    }



    /**
     * permissions 权限组
     */
    public class PermissionsBean {
        private String perm_name; //权限名称
        private String parent; //父权限名称
        private Object required_auth; //权限详情

        public String getPerm_name() {
            return perm_name;
        }

        public void setPerm_name(String perm_name) {
            this.perm_name = perm_name;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public Object getRequired_auth() {
            return required_auth;
        }

        public void setRequired_auth(Object required_auth) {
            this.required_auth = required_auth;
        }
    }

    /**
     * 权限详情
     */
    public class Required_auth {
        private int threshold; //阈值
        private List<String> keys;
        private List<Accounts> accounts; //账户权限分配信息
        private Object waits;

        public int getThreshold() {
            return threshold;
        }

        public void setThreshold(int threshold) {
            this.threshold = threshold;
        }

        public List<String> getKeys() {
            return keys;
        }

        public void setKeys(List<String> keys) {
            this.keys = keys;
        }

        public List<Accounts> getAccounts() {
            return accounts;
        }

        public void setAccounts(List<Accounts> accounts) {
            this.accounts = accounts;
        }

        public Object getWaits() {
            return waits;
        }

        public void setWaits(Object waits) {
            this.waits = waits;
        }
    }

    /**
     * 账户权限分配信息
     */
    public class Accounts {
        private Object permission;
        private int weight;
    }
}
