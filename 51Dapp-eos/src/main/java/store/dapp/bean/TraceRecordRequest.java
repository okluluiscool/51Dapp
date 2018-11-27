package store.dapp.bean;

/**
 * @Auther: liulu3
 * @Date: 2018/11/4 15:23
 * @Description: 账户的代币转账记录 request
 */
public class TraceRecordRequest {
    private String module = "account";
    private String action = "get_account_related_trx_info";
    private String account = ""; //账户名
    private String symbol = ""; //代币名称
    private String code = ""; //发行账号
    private int page = 1; //页码,默认第一页
    private int size = 20; //每页数据条数,默认20
    private String apikey = "";

    private String name;

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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return  "?module=" + module + '&' +
                "action=" + action + '&' +
                "apikey=" + apikey + '&' +
                "account=" + account + '&' +
                "symbol=" + symbol + '&' +
                "code=" + code + '&' +
                "page=" + page + '&' +
                "size=" + size ;
    }
}
