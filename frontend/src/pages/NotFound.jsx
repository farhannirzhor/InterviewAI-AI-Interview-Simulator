import React from 'react';
import { Link } from 'react-router-dom';
import { Home, AlertCircle } from 'lucide-react';
import Card from '../components/common/Card';
import Button from '../components/common/Button';

const NotFound = () => {
  return (
    <div className="flex flex-col items-center justify-center min-h-[70vh] px-4 py-8">
      <Card className="max-w-md text-center border-glow p-8 space-y-6">
        <div className="w-16 h-16 rounded-full bg-slate-800 flex items-center justify-center mx-auto text-brand-400">
          <AlertCircle className="w-8 h-8" />
        </div>
        
        <div className="space-y-2">
          <h2 className="text-3xl font-extrabold text-slate-100">404 - Page Not Found</h2>
          <p className="text-sm text-slate-400 leading-relaxed">
            The page you are looking for does not exist or has been moved to another path.
          </p>
        </div>

        <Link to="/" className="block">
          <Button variant="primary" className="w-full py-3" icon={Home}>
            Back to Dashboard
          </Button>
        </Link>
      </Card>
    </div>
  );
};

export default NotFound;
