package com.javabash.game;

public class Files {
    private File[] files;

    /**An object with methods for iterating through all
     * {@code File} objects. */
    public Files(File[] files) {
        this.files = files;
    }

    public File[] getFiles() { return files; }

    /**Trys to find and return a {@code File} by its name.
     * Method returns {@code null} if no match was found.
     * @param name : Filename
     * @return The {@code File} object with name {@code name}, or {@code null}. */
    public File getFile(String name) {
        for (File file : files) {
            if (file.getName().equals(name)) {
                return file;
            }
        }
        return null;
    }
}
