package com.example.jobportal.dto;

import java.util.List;

/**
 * Generic DTO for paginated responses
 * @param <T> The type of items in the page
 */
public class PaginatedResponse<T> {

    private List<T> content;         // The page content (list of items)
    private int pageNumber;          // Current page number (0-based)
    private int pageSize;            // Number of items per page
    private long totalElements;      // Total number of items across all pages
    private int totalPages;          // Total number of pages
    private boolean last;            // Whether this is the last page

    /**
     * Constructor for creating a paginated response
     *
     * @param content The list of items in the current page
     * @param pageNumber The current page number (0-based)
     * @param pageSize The number of items per page
     * @param totalElements The total number of items across all pages
     * @param totalPages The total number of pages
     * @param last Whether this is the last page
     */
    public PaginatedResponse(List<T> content, int pageNumber, int pageSize,
                             long totalElements, int totalPages, boolean last) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    /**
     * Default constructor
     */
    public PaginatedResponse() {
    }

    // Getters and setters

    /**
     * Get the page content
     * @return List of items in the current page
     */
    public List<T> getContent() {
        return content;
    }

    /**
     * Set the page content
     * @param content List of items for the current page
     */
    public void setContent(List<T> content) {
        this.content = content;
    }

    /**
     * Get the current page number (0-based)
     * @return The current page number
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * Set the current page number
     * @param pageNumber The page number (0-based)
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * Get the number of items per page
     * @return The page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Set the number of items per page
     * @param pageSize The page size
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Get the total number of items across all pages
     * @return The total element count
     */
    public long getTotalElements() {
        return totalElements;
    }

    /**
     * Set the total number of items across all pages
     * @param totalElements The total element count
     */
    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    /**
     * Get the total number of pages
     * @return The total page count
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Set the total number of pages
     * @param totalPages The total page count
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * Check if this is the last page
     * @return true if this is the last page, false otherwise
     */
    public boolean isLast() {
        return last;
    }

    /**
     * Set whether this is the last page
     * @param last true if this is the last page, false otherwise
     */
    public void setLast(boolean last) {
        this.last = last;
    }
}