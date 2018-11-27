package store.dapp.bean;

/**
 * @Auther: liulu3
 * @Date: 2018/11/1 20:59
 * @Description: 查询用户基本信息的request
 */
public class EosRequestBean {
    private String module; //account
    private String action; //get_account_info
    private String account; //账户名

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "EosRequestBean{" +
                "module='" + module + '\'' +
                ", action='" + action + '\'' +
                ", account='" + account + '\'' +
                '}';
    }
}
