// src/components/ui/Badge.jsx
import React from "react";

export function Badge({ children, variant = "default" }) {
    const base = "inline-flex items-center rounded-full px-3 py-1 text-sm font-medium";
    const variants = {
        default: "bg-gray-200 text-gray-900",
        success: "bg-green-100 text-green-800",
        warning: "bg-yellow-100 text-yellow-800",
        danger: "bg-red-100 text-red-800",
        info: "bg-blue-100 text-blue-800",
    };

    return (
        <span className={`${base} ${variants[variant] || variants.default}`}>
      {children}
    </span>
    );
}
