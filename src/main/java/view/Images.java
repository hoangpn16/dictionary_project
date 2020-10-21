package view;

import javafx.scene.image.Image;

public class Images {
    public static Image img_null;
    public static Image img_icon;
    public static Image img_tichxanh;
    public static Image img_search;
    public static Image img_add;
    public static Image img_sua;
    public static Image img_thongbao;
    public static Image img_xoa;
    public static Image img_loa;
    public static Image img_canhbao;

    /**
     * Load image.
     */
    public static void loadImage() {
        img_null = new Image("/images/img_null.jpg");
        img_icon = new Image("/images/img_Icon.jpg");
        img_tichxanh = new Image("/images/img_tichxanh.jpg");
        img_search = new Image("/images/img_search.jpg");
        img_add = new Image("/images/img_them.jpg");
        img_sua = new Image("/images/img_sua.jpg");
        img_thongbao = new Image("/images/img_Thongbao.jpg");
        img_xoa = new Image("/images/img_xoa.jpg");
        img_loa = new Image("/images/img_loa.jpg");
        img_canhbao = new Image("/images/img_canhbao.jpg");
    }
}

