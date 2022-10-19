package com.moengage.machinecoding.network;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.InputStream;

@SuppressWarnings("unused")
public final class OnboardingNetworkManager extends NetworkManager {
    private static final String ONBOARDING_FILENAME = "onboarding.json";

    public static byte[] getOnboardingQuestions(@NonNull Context context) {
        return getJson(context, ONBOARDING_FILENAME);
    }

    @Nullable
    public static InputStream getOnboardingQuestionsStream(@NonNull Context context) {
        return getJsonStream(context, ONBOARDING_FILENAME);
    }
}