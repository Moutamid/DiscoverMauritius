package com.moutamid.sqlapp.model;

import java.util.ArrayList;
import java.util.List;

public class BeachItem {

        public String title;
        public int main_image;
        public List<String> texts;
        public List<String> titles;
        public List<Integer> images;

        public BeachItem() {
            texts = new ArrayList<>();
            titles = new ArrayList<>();
            images = new ArrayList<>();
        }

        // Add methods to manipulate lists as needed
        public void addText(String text) {
            texts.add(text);
        }

        public void addTitle(String title) {
            titles.add(title);
        }

        public void addImage(int image) {
            images.add(image);
        }

        // Implement other methods to get data from lists based on position
        public String getText(int position) {
            return texts.get(position);
        }

        public String getTitle(int position) {
            return titles.get(position);
        }

        public int getImage(int position) {
            return images.get(position);
        }
    }
