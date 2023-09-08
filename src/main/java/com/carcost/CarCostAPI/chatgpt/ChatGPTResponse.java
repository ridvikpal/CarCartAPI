package com.carcost.CarCostAPI.chatgpt;

import java.util.ArrayList;

public class ChatGPTResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private ArrayList<Choice> choices;

    public static class Choice {
        private int index;
        private Message message;

        public static class Message {
            private String role;
            private String content;

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }

        private String finish_reason;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        public String getFinish_reason() {
            return finish_reason;
        }

        public void setFinish_reason(String finish_reason) {
            this.finish_reason = finish_reason;
        }
    }

    private Usage usage;

    private static class Usage {
        private String prompt_tokens;
        private String completion_tokens;
        private String total_tokens;

        public String getPrompt_tokens() {
            return prompt_tokens;
        }

        public void setPrompt_tokens(String prompt_tokens) {
            this.prompt_tokens = prompt_tokens;
        }

        public String getCompletion_tokens() {
            return completion_tokens;
        }

        public void setCompletion_tokens(String completion_tokens) {
            this.completion_tokens = completion_tokens;
        }

        public String getTotal_tokens() {
            return total_tokens;
        }

        public void setTotal_tokens(String total_tokens) {
            this.total_tokens = total_tokens;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public ArrayList<Choice> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<Choice> choices) {
        this.choices = choices;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }
}
