package in.adcet.event_management.DTO;

public class AdminEvent {

    String name;
    public String code;
    String description;
    String status;
    int registered;
    String bannerPath;

    public int getRegistered() {
        return registered;
    }

    public void setRegistered(int registered) {
        this.registered = registered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        if(status!=null)
            return status;
        return "Upcoming";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBannerPath() {
        return bannerPath;
    }

    public void setBannerPath(String bannerPath) {
        this.bannerPath = bannerPath;
    }

    public AdminEvent(String name, String code, String description, String status, int registered, String bannerPath) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.status = status;
        this.registered = registered;
        this.bannerPath = bannerPath;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        in.adcet.event_management.DTO.AdminEvent other = (AdminEvent) obj;
        return code.equals(other.code);
    }
}
