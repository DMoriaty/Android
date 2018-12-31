package com.julian.qplayer;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ArtistInfo {


    @SerializedName("artist")
    private ArtistEntity info;

    public void setInfo(ArtistEntity info) {
        this.info = info;
    }

    public ArtistEntity getInfo() {
        return info;
    }

    public static class ArtistEntity {


        private BioEntity bio;


        private List<ImageEntity> image;

        public void setBio(BioEntity bio) {
            this.bio = bio;
        }

        public void setImage(List<ImageEntity> image) {
            this.image = image;
        }

        public BioEntity getBio() {
            return bio;
        }

        public List<ImageEntity> getImage() {
            return image;
        }

        public static class BioEntity {
            private String summary;

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getSummary() {
                return summary;
            }
        }

        public static class ImageEntity {
            @SerializedName("#text")
            private String url="";
            private String size;

            public void setUrl(String url) {
                this.url = url;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getUrl() {
                return url;
            }

            public String getSize() {
                return size;
            }
        }
    }
}
