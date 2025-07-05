package com.cibertec.pos_system.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {
    
    private static final String UPLOAD_DIR = "src/main/resources/static/home-ventas/img/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_TYPES = {
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    };
    
    public String uploadImage(MultipartFile file) throws IOException {
        // Validate file
        validateFile(file);
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + extension;
        
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Save file
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return uniqueFilename;
    }
    
    private void validateFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("No se ha seleccionado ningún archivo");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("El archivo es demasiado grande. Tamaño máximo: 5MB");
        }
        
        String contentType = file.getContentType();
        boolean isValidType = false;
        for (String allowedType : ALLOWED_TYPES) {
            if (allowedType.equals(contentType)) {
                isValidType = true;
                break;
            }
        }
        
        if (!isValidType) {
            throw new IOException("Tipo de archivo no permitido. Solo se permiten imágenes (JPG, PNG, GIF, WEBP)");
        }
    }
    
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
    
    public void deleteImage(String filename) {
        if (filename != null && !filename.trim().isEmpty()) {
            try {
                Path filePath = Paths.get(UPLOAD_DIR, filename);
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }
            } catch (IOException e) {
                // Log error but don't throw exception
                System.err.println("Error deleting file: " + filename + " - " + e.getMessage());
            }
        }
    }
} 