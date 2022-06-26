package ServiceLayer.Objects;

public record Notification(String Content){

    public Notification(BusinessLayer.Notifications.Notification not){
        this(not.getContent());
    }

    public Notification(String Content) {
        this.Content = Content;
    }
}
