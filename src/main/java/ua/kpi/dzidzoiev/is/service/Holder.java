package ua.kpi.dzidzoiev.is.service;

import ua.kpi.dzidzoiev.is.controller.MainController;

import java.nio.charset.Charset;
import java.util.logging.Logger;

/**
 * Created by dzidzoiev on 12/7/15.
 */
public class Holder {
    public static final Logger Log = Logger.getLogger(MainController.class.getName());
    public static final Charset UTF_8 = Charset.forName("UTF-8");
}
