package org.example.rabbitmq.api.utils;

public class RabbitConstant {
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 5672;
    public static final String VIRTUAL_HOST = "/test";
    public static final String USERNAME = "test";
    public static final String PASSWORD = "test";

    public static final String QUEUE_HELLO_WORLD = "hello_world";
    public static final String QUEUE_SMS = "sms";

    public static final String EXCHANGE_WEATHER = "exchange_weather";
    public static final String QUEUE_WEATHER_SINA = "queue_weather_sina";
    public static final String QUEUE_WEATHER_BAIDU = "queue_weather_baidu";

    public static final String EXCHANGE_LOG_ROUTING = "exchange_log_routing";
    public static final String QUEUE_LOG_ERROR = "queue_log_error";
    public static final String QUEUE_LOG_OTHER = "queue_log_other";

    public static final String EXCHANGE_NEWS_TOPIC = "exchange_news_topic";
    public static final String QUEUE_NEWS_HEFEI = "queue_news_hefei";
    public static final String QUEUE_NEWS_SHANGHAI = "queue_news_shanghai";

}
