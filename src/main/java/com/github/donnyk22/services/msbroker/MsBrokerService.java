package com.github.donnyk22.services.msbroker;

import com.github.donnyk22.models.forms.MsBrokerForm;

public interface MsBrokerService {
    MsBrokerForm sendToTopicObject(MsBrokerForm object);
    String sendToTopicText(String text);
}
