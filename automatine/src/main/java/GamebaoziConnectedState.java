public class GamebaoziConnectedState extends GamebaoziState {

    @Override
    public void onCommand(NetCommand cmd) {

        client.ChangeTo(new GamebaoziRoleSelectedState());
    }


}
