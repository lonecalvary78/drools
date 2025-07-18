/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.drools.compiler.compiler.io.memory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.drools.compiler.compiler.io.File;
import org.drools.compiler.compiler.io.Folder;
import org.drools.io.InputStreamResource;
import org.drools.util.PortablePath;

public class MemoryFile implements File,
                                   Serializable {
    private String name;
    private Folder folder;
    private MemoryFileSystem mfs;

    public MemoryFile( MemoryFileSystem mfs, String name, Folder folder) {
        this.name = name;
        this.folder = folder;
        this.mfs = mfs;
    }
    
    public String getName() {
        return name;
    }         
    
    public InputStream getContents()  throws IOException {
        if ( !exists() ) {
            throw new IOException("File does not exist, unable to open InputStream" );
        }
        return new ByteArrayInputStream( mfs.getFileContents( this ) );
    }
    
    public PortablePath getPath() {
        return folder.getPath().resolve(name);
    }            
    
    public Folder getFolder() {
        return this.folder;
    }
    
    public boolean exists() {
        return mfs.existsFile( getPath() );
    }        


    public void setContents(InputStream is) throws IOException {   
        if ( !exists() ) {
            throw new IOException( "File does not exists, cannot set contents" );
        }
        
        mfs.setFileContents( this, new InputStreamResource(is) );
    }

    public void create(InputStream is) throws IOException {
        mfs.setFileContents( this, new InputStreamResource(is) );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((folder == null) ? 0 : folder.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        MemoryFile other = (MemoryFile) obj;
        if ( folder == null ) {
            if ( other.folder != null ) {
                return false;
            }
        } else if ( !folder.equals( other.folder ) ) {
            return false;
        }
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "MemoryFile [name=" + name + ", folder=" + folder + "]";
    }
    
}
