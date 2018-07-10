public class MessageClass {
    public int cmdCode;
    public CommandSet.ParsingMessage parse;
    public Class<?> cla;

    public MessageClass(int cmdCode, CommandSet.ParsingMessage parse, Class<?> cla) {
        this.cmdCode = cmdCode;
        this.parse = parse;
        this.cla = cla;
    }
}