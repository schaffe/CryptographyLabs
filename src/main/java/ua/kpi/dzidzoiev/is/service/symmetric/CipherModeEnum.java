package ua.kpi.dzidzoiev.is.service.symmetric;

/**
 * Created by dzidzoiev on 12/1/15.
 */
public enum CipherModeEnum {
    ECB(1),
    CBC(2),
    PCBC(3),
    CFB(4),
    OFB(5);

    private int order;

    CipherModeEnum(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }


}
