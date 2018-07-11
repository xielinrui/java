public class NetCommand {
    private int _cmdCode;

    public int getCode() {
        return this._cmdCode;
    }

    public NetCommand(int cmdCode) {
        this._cmdCode = cmdCode;
    }

    public void parseFrom(byte[] data) throws Exception {
    }

    public void toBytes() {
        try {
//            logger.info("accept:=========命令为：{}",this._cmdCode);
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

}