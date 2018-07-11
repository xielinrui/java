public class GamebaoziRoleSelectedState extends GamebaoziState {


    @Override
    public void onCommand(NetCommand cmd) {
        client.ChangeTo(new GamebaoziPlayState());
    }

}