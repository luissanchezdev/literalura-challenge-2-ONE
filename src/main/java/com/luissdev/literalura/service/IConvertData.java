package com.luissdev.literalura.service;

public interface IConvertData {
    <T> T getData(String json, Class<T> tClass);
}
