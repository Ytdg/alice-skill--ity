package com.hackaton.alicecity.dto;

//все запросы кроме партнерских ссылок, имеют command!=null
public enum TypeRequest {
    ButtonPressed,
    SimpleUtterance;

}
