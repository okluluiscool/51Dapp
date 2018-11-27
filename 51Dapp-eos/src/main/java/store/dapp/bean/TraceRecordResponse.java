package store.dapp.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: liulu3
 * @Date: 2018/11/4 15:24
 * @Description: 账户的代币转账记录 response
 */
public class TraceRecordResponse {
    private int trace_count; //代币转账记录条数
    private List<Trace> trace_list; //代币转账记录

    public int getTrace_count() {
        return trace_count;
    }

    public void setTrace_count(int trace_count) {
        this.trace_count = trace_count;
    }

    public List<Trace> getTrace_list() {
        return trace_list;
    }

    public void setTrace_list(List<Trace> trace_list) {
        this.trace_list = trace_list;
    }

    @Override
    public String toString() {
        return "TraceRecordResponse{" +
                "trace_count=" + trace_count +
                ",trace_list=" + trace_list +
                '}';
    }

    public class Trace{
        private String trx_id; //交易id
        private String timestamp; //交易时间
        private String receiver; //代币接受者
        private String sender; //代币发送者(当发送者为自己时为转出反之为转入)
        private String code; //发币账户
        private String quantity; //金额
        private Object memo; //转账备注
        private String symbol; //代币名称
        private String status; //转账状态

        public String getTrx_id() {
            return trx_id;
        }

        public void setTrx_id(String trx_id) {
            this.trx_id = trx_id;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public Object getMemo() {
            return memo;
        }

        public void setMemo(Object memo) {
            this.memo = memo;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Trace{" +
                    "trx_id=" + trx_id +
                    ",timestamp=" + timestamp +
                    ",receiver=" + receiver +
                    ",sender=" + sender +
                    ",code=" + code +
                    ",quantity=" + quantity +
                    ",memo=" + memo +
                    ",symbol=" + symbol +
                    ",status=" + status +
                    '}';
        }

    }
}
