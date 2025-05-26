package dev.tt.poc.simpletask;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface MyActivity {
    @ActivityMethod
    String execute(String input);
}