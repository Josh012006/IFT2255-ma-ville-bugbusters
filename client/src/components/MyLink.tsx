import type { ReactNode } from "react";
import { Link, type LinkProps } from "react-router-dom";

type MyLinkProps = {
  to: string;
  className?: string;
  children: ReactNode;
} & Omit<LinkProps, "to" | "className" | "children">;

export default function MyLink({ to, className = "", children, ...rest }: MyLinkProps) {
  return (
    <Link className={`text-decoration-none  ${className}`} to={to} {...rest}>
      {children}
    </Link>
  );
}
