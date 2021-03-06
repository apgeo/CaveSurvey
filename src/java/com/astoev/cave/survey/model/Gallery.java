package com.astoev.cave.survey.model;

import com.astoev.cave.survey.util.DaoUtil;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: astoev
 * Date: 3/31/12
 * Time: 9:47 PM
 * To change this template use File | Settings | File Templates.
 */
@DatabaseTable(tableName = "galleries")
public class Gallery implements Serializable {

	private static final long serialVersionUID = 201312130309L;
	
    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";


    private static final char GALLERY_LETTERS[] = new char[26];
    private static final char GALLERY_LAST_LETTER = 'Z';
    static {
        int index = 0;
        for (char c = 'A'; c <= 'Z'; c++) {
            GALLERY_LETTERS[index++] = c;
        }
    }

    public Gallery() {
    }

    @DatabaseField(generatedId = true, columnName = COLUMN_ID)
    private Integer mId;

    @DatabaseField(foreign = true, canBeNull = false, foreignAutoRefresh = true, columnName = COLUMN_PROJECT_ID)
    private Project mProject;

    @DatabaseField(columnName = COLUMN_NAME)
    private String mName;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer aId) {
        mId = aId;
    }


    public String getName() {
        return mName;
    }

    public void setName(String aName) {
        mName = aName;
    }

    public Project getProject() {
        return mProject;
    }

    public void setProject(Project aProject) {
        mProject = aProject;
    }

    // "" as starting
    public static String getFirstGalleryName() {
        return "A";
    }

    // A -> B ... -> Z -> AA -> AB etc for next galleries
    public static String generateNextGalleryName(Integer aProjectId) throws SQLException {
        Gallery lastGallery = DaoUtil.getLastGallery(aProjectId);
        
        return nextName( lastGallery.getName());
    }

    public static String nextName(String s) {
        if (getFirstGalleryName().equals(s)) {
            // start with A->B
            return String.valueOf(GALLERY_LETTERS[1]);
        } else {
            // try to push A up to Z for each position
            for (int i = s.length()-1; i>=0; i--) {
               if (s.charAt(i) < GALLERY_LAST_LETTER) {
                  StringBuilder name = new StringBuilder();
                   if (s.length()>1 && i<=s.length()-1) {
                                  name.append(s.substring(0, i));
                   }
                   name.append((char)(((int)s.charAt(i)) + 1));
                   if (s.length() >1 && i<=s.length()-2) {
                       name.append(s.substring(i));
                   }
                   return name.toString();
               }
            }
            // add more positions if all positions Z
            return GALLERY_LETTERS[0] + s;
        }
    }

    @Override
    public String toString() {
        return "Gallery{" +
                "mId=" + mId +
                ", mProject=" + mProject +
                ", mName='" + mName + '\'' +
                '}';
    }
}
