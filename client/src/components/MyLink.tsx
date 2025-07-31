import type { ReactNode } from "react";
import { Link, type LinkProps } from "react-router-dom";


type MyLinkProps = {
  /**qui représente l'url du lien */
  to: string;
  /**qui est l'attribut class à mettre sur le composant */
  className?: string;
  /**qui est l'enfant du composant, donc l'élement affiché par le lien */
  children: ReactNode;
} & Omit<LinkProps, "to" | "className" | "children">;


/**
 * Une classe wrapper qui offre un style préfait pour les liens HTML.
 * Elle représente un lien dans le routage.
 * @return ReactNode.
 */
export default function MyLink(props: MyLinkProps) {

  const { to, className = "", children, ...rest } = props;

  return (
    <Link className={`text-decoration-none  ${className}`} to={to} {...rest}>
      {children}
    </Link>
  );
}
