import React from 'react';

const Button = ({
  children,
  onClick,
  type = 'button',
  variant = 'primary',
  size = 'md',
  disabled = false,
  className = '',
  icon: Icon,
  ...props
}) => {
  const baseStyle = 'inline-flex items-center justify-center font-medium rounded-xl transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-dark-950 focus:ring-brand-500 disabled:opacity-50 disabled:pointer-events-none active:scale-[0.98]';
  
  const variants = {
    primary: 'bg-gradient-to-r from-brand-600 to-violet-600 text-white hover:from-brand-500 hover:to-violet-500 shadow-md shadow-brand-900/20 hover:shadow-brand-500/25',
    secondary: 'bg-dark-700 text-slate-100 hover:bg-dark-600 border border-dark-600',
    danger: 'bg-red-600 text-white hover:bg-red-500 hover:shadow-lg hover:shadow-red-500/25',
    outline: 'bg-transparent text-brand-400 border border-brand-500/40 hover:bg-brand-500/10 hover:border-brand-500',
    glass: 'bg-white/5 backdrop-blur-md border border-white/10 text-slate-200 hover:bg-white/10 hover:border-white/20',
  };

  const sizes = {
    sm: 'px-3 py-1.5 text-xs',
    md: 'px-4 py-2.5 text-sm',
    lg: 'px-6 py-3.5 text-base',
  };

  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`${baseStyle} ${variants[variant]} ${sizes[size]} ${className}`}
      {...props}
    >
      {Icon && <Icon className={`w-4 h-4 mr-2 ${children ? '' : 'mr-0'}`} />}
      {children}
    </button>
  );
};

export default Button;
