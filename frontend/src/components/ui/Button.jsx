// src/components/ui/Button.jsx
import React from "react";

export function Button({
                           children,
                           variant = "default",
                           size = "md",
                           className = "",
                           ...props
                       }) {
    const base =
        "inline-flex items-center justify-center font-medium rounded-lg transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 disabled:opacity-50 disabled:pointer-events-none";

    const variants = {
        default: "bg-blue-600 text-white hover:bg-blue-700 focus:ring-blue-500",
        outline:
            "border border-gray-300 bg-white text-gray-700 hover:bg-gray-100 focus:ring-gray-400",
        ghost:
            "bg-transparent text-gray-700 hover:bg-gray-100 focus:ring-gray-400",
        destructive:
            "bg-red-600 text-white hover:bg-red-700 focus:ring-red-500",
    };

    const sizes = {
        sm: "px-2.5 py-1.5 text-sm",
        md: "px-4 py-2 text-sm",
        lg: "px-6 py-3 text-base",
    };

    return (
        <button
            className={`${base} ${variants[variant]} ${sizes[size]} ${className}`}
            {...props}
        >
            {children}
        </button>
    );
}
