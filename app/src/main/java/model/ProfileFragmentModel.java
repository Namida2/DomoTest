package model;

import java.util.concurrent.atomic.AtomicBoolean;

import interfaces.ProfileFragmentInterface;

public class ProfileFragmentModel implements ProfileFragmentInterface.Model {

    public static AtomicBoolean NEED_NOTIFY = new AtomicBoolean(true);
}
