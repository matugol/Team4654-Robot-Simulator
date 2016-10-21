package com.qualcomm.robotcore.hardware;

/**
 * Created by bob on 2016-03-12.
 */
public class PWMOutputImplEx extends PWMOutputImpl implements PWMOutputEx
    {
    PWMOutputControllerEx controllerEx;

    public PWMOutputImplEx(PWMOutputController controller, int port)
        {
        super(controller, port);
        this.controllerEx = (PWMOutputControllerEx)controller;
        }

    @Override
    public void setPwmEnable()
        {
        this.controllerEx.setPwmEnable(this.port);
        }

    @Override
    public void setPwmDisable()
        {
        this.controllerEx.setPwmDisable(this.port);
        }

    @Override
    public boolean isPwmEnabled()
        {
        return this.controllerEx.isPwmEnabled(this.port);
        }
    }
