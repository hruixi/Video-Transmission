package com.rui.mvvm_jetpack.model.bean;

import java.util.List;

/**
 * @ClassName RedditBean
 * @Description TODO
 * @Author He ruixiang
 * @Date 2020/5/9 16:55
 */
public class RedditBean {
    private RedditDataBean data;

    public RedditDataBean getData() {
        return data;
    }

    public static class RedditDataBean {
        private List<ChildrenBean> children;
        private String after;
        private String before;

        public List<ChildrenBean> getChildren() {
            return children;
        }

        public String getAfter() {
            return after;
        }

        public String getBefore() {
            return before;
        }

        public static class ChildrenBean {
            private ChildrenDataBean data;

            public ChildrenDataBean getData() {
                return data;
            }

            public static class ChildrenDataBean {
                private String name;
                private String title;
                private int score;
                private String author;
                private String subreddit;
                private int num_comments;
                private long created;
                private String thumbnail;
                private String url;

                public String getName() {
                    return name;
                }

                public String getTitle() {
                    return title;
                }

                public int getScore() {
                    return score;
                }

                public String getAuthor() {
                    return author;
                }

                public String getSubreddit() {
                    return subreddit;
                }

                public int getNum_comments() {
                    return num_comments;
                }

                public long getCreated() {
                    return created;
                }

                public String getThumbnail() {
                    return thumbnail;
                }

                public String getUrl() {
                    return url;
                }
            }
        }
    }
}




