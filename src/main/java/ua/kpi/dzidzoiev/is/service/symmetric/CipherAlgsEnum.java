package ua.kpi.dzidzoiev.is.service.symmetric;

/**
 * Created by dzidzoiev on 12/1/15.
 */
public enum  CipherAlgsEnum {
    DES(1),
    AES(2),
    DSTU_7426(3) {
        @Override
        public String toString() {
            return "ДСТУ 7624:2014";
        }
    };

    private int order;

    CipherAlgsEnum(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
